Every task was done except finding duplicates via java 7 parallel.
Here's some performance tests:

Lists with integer and string, both 500 random elements, limit 500: 
Java 7 aggregator:
Sum time: 1 millis
Frequency time: 13 millis
Duplicates time: 59 millis

Java 7 parallel aggregator:
Sum time: 3 millis
Frequency time: 10 millis

Java 8 aggregator:
Sum time: 0 millis
Frequency time: 12 millis
Duplicates time: 2 millis

Java 8 parallel aggregator:
Sum time: 2 millis
Frequency time: 9 millis
Duplicates time: 4 millis

5000 elements, limit 5000: 
Java 7 aggregator:
Sum time: 1 millis
Frequency time: 21 millis
Duplicates time: 58 millis

Java 7 parallel aggregator:
Sum time: 4 millis
Frequency time: 23 millis

Java 8 aggregator:
Sum time: 2 millis
Frequency time: 25 millis
Duplicates time: 6 millis

Java 8 parallel aggregator:
Sum time: 2 millis
Frequency time: 26 millis
Duplicates time: 16 millis

50_000 elements, 50_000 limit:
Java 7 aggregator:
Sum time: 10 millis
Frequency time: 140 millis
Duplicates time: 124 millis

Java 7 parallel aggregator:
Sum time: 9 millis
Frequency time: 105 millis

Java 8 aggregator:
Sum time: 5 millis
Frequency time: 133 millis
Duplicates time: 85 millis

Java 8 parallel aggregator:
Sum time: 4 millis
Frequency time: 104 millis
Duplicates time: 70 millis

500_000 elements, limit 500_000:
Java 7 aggregator:
Sum time: 12 millis
Frequency time: 829 millis
Duplicates time: 821 millis

Java 7 parallel aggregator:
Sum time: 22 millis
Frequency time: 659 millis

Java 8 aggregator:
Sum time: 8 millis
Frequency time: 763 millis
Duplicates time: 800 millis

Java 8 parallel aggregator:
Sum time: 12 millis
Frequency time: 498 millis
Duplicates time: 590 millis

2_000_000 elements, limit 2_000_000:
Java 7 aggregator:
Sum time: 14 millis
Frequency time: 3840 millis
Duplicates time: 4426 millis

Java 7 parallel aggregator:
Sum time: 91 millis
Frequency time: 3720 millis

Java 8 aggregator:
Sum time: 16 millis
Frequency time: 3624 millis
Duplicates time: 4408 millis

Java 8 parallel aggregator:
Sum time: 18 millis
Frequency time: 1971 millis
Duplicates time: 2870 millis

5_000_000 elements, limit 5_000_000:
Java 7 aggregator:
Sum time: 16 millis
Frequency time: 13190 millis
Duplicates time: 14588 millis

Java 7 parallel aggregator:
Sum time: 33 millis
Frequency time: 9814 millis

Java 8 aggregator:
Sum time: 40 millis
Frequency time: 10981 millis
Duplicates time: 13992 millis

Java 8 parallel aggregator:
Sum time: 21 millis
Frequency time: 7142 millis
Duplicates time: 9674 millis