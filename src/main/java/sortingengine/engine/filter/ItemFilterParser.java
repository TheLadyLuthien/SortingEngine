package sortingengine.engine.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sortingengine.engine.data.item.Item;
import sortingengine.engine.data.tag.Tag;
import sortingengine.engine.data.tag.TagCatagories;
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

    private static final Map<String, Function<String, Predicate<Item>>> FILTER_CODE_MAP = new HashMap<>();

    public static void init()
    {
        FILTER_CODE_MAP.put("lt", (tag) -> ItemFilter.tagMatch(TagCatagories.LOCATION, Tag.of(tag)));
        FILTER_CODE_MAP.put("dt", (tag) -> ItemFilter.tagMatch(TagCatagories.DATE, Tag.of(tag)));
        FILTER_CODE_MAP.put("ct", (tag) -> ItemFilter.tagMatch(TagCatagories.CONTENT, Tag.of(tag)));
    }

    static {
        init();
    }

    public static Predicate<Item> parse(String str)
    {
        // String test = Matcher.quoteReplacement(AND_TOKEN);
        String string = str.replace(" ", "");

        final String[] tokens = string.split(SPLITTER_REGEX);

        return parseTokenSet(0, tokens);
    }

    private static Predicate<Item> parseTokenSet(int i, String[] tokens)
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
                    // not a special token, so apply predicate
                    final String[] bits = token.split(":");
                    final String filterType = bits[0];
                    final String filterString = bits[1];

                    Predicate<Item> result = FILTER_CODE_MAP.get(filterType).apply(filterString);
                    finalizedList.add(result);
                }
                    break;
            }
        }

        return mergeFinalizedList(finalizedList);
    }

    private static Predicate<Item> mergeFinalizedList(ArrayList<Object> finalizedList)
    {
        // finalizedList can only contain Predicate<Item> or String
        if (finalizedList.size() == 1)
        {
            return ((Predicate<Item>)finalizedList.get(0));
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
                        
                        if (a instanceof Predicate && b instanceof Predicate)
                        {
                            Predicate<Item> and = ((Predicate<Item>)a).and((Predicate<Item>)b);
                            nextLayerList.add(and);
                        }
                    }
                    break;

                    case OR_TOKEN:
                    {
                        Object a = finalizedList.get(i - 1);
                        Object b = finalizedList.get(i + 1);
                        
                        if (a instanceof Predicate && b instanceof Predicate)
                        {
                            Predicate<Item> or = ((Predicate<Item>)a).or((Predicate<Item>)b);
                            nextLayerList.add(or);
                        }
                    }
                    break;

                    case NOT_TOKEN:
                    {
                        Object next = finalizedList.get(i + 1);
                        
                        if (next instanceof Predicate)
                        {
                            Predicate<Item> not = ((Predicate<Item>)next).negate();
                            nextLayerList.add(not);
                        }
                    }
                    break;
                }
            }

            // Object next = finalizedList.get(i + 1);
            // if ((o instanceof Predicate) && (!))
            // {
            //     nextLayerList.add(o);
            // }
        }

        return mergeFinalizedList(nextLayerList);
    }
}
