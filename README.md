## Readme first

This package is licensed under the Apache Software License, version 2.0.

See the file `LICENSE` at the root of this package.

This package requires **Java 8**.

## What this is

This package aims to emulate Guava's
[`Range`](http://google.github.io/guava/releases/19.0/api/docs/com/google/common/collect/Range.html)
for the following primitive types:

* `char`,
* `int`,
* `long`,
* `double`.

It also aims at providing, to the extent possible, an equivalent of Guava's
`RangeSet` and `RangeMap` for said primitive types (and their `Immutable`
counterparts as well).

## Motivation

`Range` is an immensely powerful API. Unfortunately, when it comes to primitive
types, you have no choice but to use boxed types for primitives, therefore you
need to have a `Range<Integer>` instead of, say, an `IntRange` as this package
aims to provide.

This results in code which is suboptimized for primitives; for instance, to
check whether an int is within a `Range<Integer>`, the code will end up making
calls to `.compareTo()` whereas simple arithmetic operations would do the job
just as well, and at a lesser cost.

