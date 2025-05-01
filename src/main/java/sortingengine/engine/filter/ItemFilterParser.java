package sortingengine.engine.filter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sortingengine.engine.data.item.Item;
import sortingengine.engine.data.tag.Tag;
import sortingengine.engine.data.tag.TagCatagory;
import sortingengine.engine.data.tag.TagCatagory;

public class ItemFilterParser
{
    private static final String GROUPING_OPEN_TOKEN = "(";
    private static final String GROUPING_CLOSE_TOKEN = ")";
    private static final String AND_TOKEN = "&";
    private static final String NOT_TOKEN = "!";
    private static final String OR_TOKEN = "|";

    private static final String SPLITTER_REGEX_CHAR_SUBSET = Pattern.quote(GROUPING_OPEN_TOKEN) + Pattern.quote(GROUPING_CLOSE_TOKEN) + Pattern.quote(AND_TOKEN) + Pattern.quote(OR_TOKEN) + Pattern.quote(NOT_TOKEN);

    private static final String SPLITTER_REGEX = "(?=[" + SPLITTER_REGEX_CHAR_SUBSET + "])|(?<=[" + SPLITTER_REGEX_CHAR_SUBSET + "])";

    private static final Map<String, Function<String, ItemFilter>> FILTER_CODE_MAP = new HashMap<>();

    private static final DateTimeFormatter DATE_TIME_INPUT_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy");
    public static void init()
    {
        FILTER_CODE_MAP.put("lt", (tag) -> ItemFilter.tagMatch(TagCatagory.LOCATION, Tag.of(tag)));
        FILTER_CODE_MAP.put("dt", (tag) -> ItemFilter.tagMatch(TagCatagory.DATE, Tag.of(tag)));
        FILTER_CODE_MAP.put("ct", (tag) -> ItemFilter.tagMatch(TagCatagory.CONTENT, Tag.of(tag)));
        FILTER_CODE_MAP.put("st", (tag) -> ItemFilter.tagMatch(TagCatagory.SOURCE, Tag.of(tag)));
        FILTER_CODE_MAP.put("type", (type) -> ItemFilter.typeMatch(type));
        FILTER_CODE_MAP.put("date", (rangeString) -> {
            String[] parts = rangeString.split("-");

            LocalDate start = LocalDate.parse(parts[0], DATE_TIME_INPUT_FORMAT);
            LocalDate end = LocalDate.parse(parts[1], DATE_TIME_INPUT_FORMAT);
            
            return ItemFilter.customDateMatch(start, end);
        });
    }

    static {
        init();
    }

    public static ItemFilter parse(String str)
    {
        // String test = Matcher.quoteReplacement(AND_TOKEN);
        String string = str.replace(" ", "");

        final String[] tokens = string.split(SPLITTER_REGEX);

        return parseTokenSet(0, tokens);
    }

    private static ItemFilter parseTokenSet(int i, String[] tokens)
    {
        ArrayList<Object> finalizedList = new ArrayList<>();

        for (; i < tokens.length; i++)
        {
            final String token = tokens[i];
            switch (token)
            {
                case GROUPING_OPEN_TOKEN:
                    finalizedList.add(parseTokenSet(i + 1, tokens));
                    break;

                case GROUPING_CLOSE_TOKEN:
                    return mergeFinalizedList(finalizedList);

                case AND_TOKEN:
                case NOT_TOKEN:
                case OR_TOKEN:
                    finalizedList.add(token);
                    break;

                default:
                {
                    // not a special token, so apply ItemFilter
                    final String[] bits = token.split(":");
                    final String filterType = bits[0];
                    final String filterString = bits[1];

                    ItemFilter result = FILTER_CODE_MAP.get(filterType).apply(filterString);
                    finalizedList.add(result);
                }
                    break;
            }
        }

        return mergeFinalizedList(finalizedList);
    }

    private static ItemFilter mergeFinalizedList(ArrayList<Object> finalizedList)
    {
        // finalizedList can only contain ItemFilter or String
        if (finalizedList.size() == 1)
        {
            return ((ItemFilter)finalizedList.get(0));
        }

        ArrayList<Object> nextLayerList = new ArrayList<>();

        for (int i = 0; i < finalizedList.size(); i++)
        {
            Object o = finalizedList.get(i);
            if (o instanceof String string)
            {
                switch (string)
                {
                    case AND_TOKEN:
                    {
                        Object a = finalizedList.get(i - 1);
                        Object b = finalizedList.get(i + 1);
                        
                        if (a instanceof ItemFilter ifA && b instanceof ItemFilter ifB)
                        {
                            ItemFilter and = ifA.and(ifB);
                            nextLayerList.add(and);
                        }
                    }
                    break;

                    case OR_TOKEN:
                    {
                        Object a = finalizedList.get(i - 1);
                        Object b = finalizedList.get(i + 1);
                        
                        if (a instanceof ItemFilter ifA && b instanceof ItemFilter ifB)
                        {
                            ItemFilter or = ifA.or(ifB);
                            nextLayerList.add(or);
                        }
                    }
                    break;

                    case NOT_TOKEN:
                    {
                        Object next = finalizedList.get(i + 1);
                        
                        if (next instanceof ItemFilter filter)
                        {
                            ItemFilter not = filter.negate();
                            nextLayerList.add(not);
                        }
                    }
                    break;
                }
            }
        }

        return mergeFinalizedList(nextLayerList);
    }
}
