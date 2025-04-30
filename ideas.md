@a = any
@p = photo
@v = video
@o = other

tag @p[lt:scotland/edinburgh ct:cat|book]

## filters
& = and
| = or
! = not
() = grouping

lt: = location tag
    root/path&root2/path2
dt: = date tag
ct: = content tag
dt = device tag

date: = custom date range
    07/04/20204-07/05/2024


In edinburgh from summer 2022 and not of books, lego, or sheep
(lt:scotland/edinburgh&dt:tips/summer_2022) & !(c t:books|lego|sheep)


