import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TimetableTests {
    public static class Pair<K, V> {
        public final K left;
        public final V right;

        public Pair(K left, V right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return left + " - " + right;
        }
    }

    /**
     * @param dayStart       Начало рабочего дня.
     * @param dayEnd         Конец рабочего дня.
     * @param unit           Продолжительность цикла обслуживания.
     * @param breakStarts    Список с временами начала каждого перерыва.
     * @param breakDurations Список с продолжительностями каждого перерыва.
     */
    public static List<Pair<LocalTime, LocalTime>> buildTimetable(
            LocalTime dayStart,
            LocalTime dayEnd,
            Integer unit,
            LocalTime[] breakStarts,
            Duration[] breakDurations
    ) {
        // =========================
        // Проверка входных данных
        if (dayEnd.isBefore(dayStart)) {
            throw new IllegalArgumentException("day end is before the day start");
        }
        if (unit == 0) {
            throw new IllegalArgumentException("unit == 0");
        }
        if (breakStarts.length != breakDurations.length) {
            throw new IllegalArgumentException("breakStarts.length != breakDurations.length");
        }
        for (int i = 0; i < breakStarts.length; i++) {
            if (breakStarts[i].isBefore(dayStart) || breakStarts[i].isAfter(dayEnd)) {
                throw new IllegalArgumentException("break start #" + i + " is out of day bounds");
            }
            if (breakDurations[i].isZero()) {
                throw new IllegalArgumentException("break duration #" + i + " is zero");
            }
            if (breakStarts[i].plus(breakDurations[i]).isAfter(dayEnd)) {
                throw new IllegalArgumentException("break end #" + i + " is out of day bounds");
            }
        }
        // =========================
        // Построение расписания
        var totalMinutes = Duration.between(dayStart, dayEnd).toMinutes();
        var unitCount = totalMinutes / unit;
        var unitDuration = Duration.ofMinutes(unit);
        var results = new ArrayList<Pair<LocalTime, LocalTime>>();
        var unitStart = dayStart;
        var breakIndex = 0;
        for (var i = 1; i <= unitCount; i++) {
            var unitEnd = dayStart.plus(unitDuration.multipliedBy(i));
            if (!unitEnd.isAfter(unitStart))
                continue;
            if (breakIndex >= breakStarts.length) {
                results.add(new Pair<>(unitStart, unitEnd));
                unitStart = unitEnd;
                continue;
            }
            var breakStart = breakStarts[breakIndex];
            var breakEnd = breakStart.plus(breakDurations[breakIndex]);
            if (breakStart.isAfter(unitEnd)) {
                results.add(new Pair<>(unitStart, unitEnd));
                unitStart = unitEnd;
                continue;
            }
            var ordered = Stream.of(unitStart, breakStart, breakEnd, unitEnd)
                    .distinct()
                    .sorted(LocalTime::compareTo)
                    .collect(toList());
            for (var k = 0; k < ordered.size() - 1; k++) {
                results.add(new Pair<>(ordered.get(k), ordered.get(k + 1)));
            }
            unitStart = ordered.get(ordered.size() - 1);
            breakIndex++;
        }
        return results;
    }

    /**
     * @param dayStartString       Начало рабочего дня (HH:MM[:SS]).
     * @param dayEndString         Конец рабочего дня (HH:MM[:SS]).
     * @param unitString           Продолжительность цикла обслуживания (целое, в минутах).
     * @param breakStartsString    Список с временами начала каждого перерыва (HH:MM[:SS]).
     * @param breakDurationsString Список с продолжительностями каждого перерыва (в минутах).
     */
    public static List<Pair<LocalTime, LocalTime>> buildTimetable(
            String dayStartString,
            String dayEndString,
            String unitString,
            String breakStartsString,
            String breakDurationsString
    ) {
        // =========================
        // Подготовка входных данных
        var dayStart = LocalTime.parse(dayStartString);
        var dayEnd = LocalTime.parse(dayEndString);
        var unit = Integer.parseInt(unitString);
        var breakStarts = breakStartsString.isBlank()
                ? new LocalTime[0]
                : Arrays.stream(breakStartsString.split(","))
                .map(LocalTime::parse)
                .toArray(LocalTime[]::new);
        var breakDurations = breakDurationsString.isBlank()
                ? new Duration[0]
                : Arrays.stream(breakDurationsString.split(","))
                .map(Integer::parseInt)
                .map(Duration::ofMinutes)
                .toArray(Duration[]::new);
        // =========================
        // Построение расписания
        return buildTimetable(dayStart, dayEnd, unit, breakStarts, breakDurations);
    }

    public static String timetableToString(List<Pair<LocalTime, LocalTime>> pieces) {
        return pieces.stream().map(Pair::toString).collect(joining("\n"));
    }

    // ==================================================
    // Example 1:
    //  10:00 - 60 min
    //  12:10 - 10 min
    //  15:00 - 10 min
    //  15:30 - 40 min
    private static final String INPUT_1 =
            "09:00;"
                    + "17:00;"
                    + "30;"
                    + "10:00,12:10,15:00,15:30;"
                    + "60,10,10,40";
    private static final String EXPECTED_1 =
            "09:00 - 09:30,"
                    + "09:30 - 10:00,"
                    + "10:00 - 11:00,"
                    + "11:00 - 11:30,"
                    + "11:30 - 12:00,"
                    + "12:00 - 12:10,"
                    + "12:10 - 12:20,"
                    + "12:20 - 12:30,"
                    + "12:30 - 13:00,"
                    + "13:00 - 13:30,"
                    + "13:30 - 14:00,"
                    + "14:00 - 14:30,"
                    + "14:30 - 15:00,"
                    + "15:00 - 15:10,"
                    + "15:10 - 15:30,"
                    + "15:30 - 16:10,"
                    + "16:10 - 16:30,"
                    + "16:30 - 17:00";

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            INPUT_1 + ";" + EXPECTED_1,
    })
    public void timetableShouldBuildWhenSpecificationIsValid(
            String startString,
            String endString,
            String unitString,
            String breakStartsString,
            String breakDurationsString,
            String expected
    ) {
        var timetable = buildTimetable(
                startString,
                endString,
                unitString,
                breakStartsString,
                breakDurationsString
        );
        assertEquals(expected.replace(',', '\n'), timetableToString(timetable));
    }

    @Test
    public void timetableShouldBuildWhenNoBreaksArePassed() {
        var timetable = buildTimetable("12:00", "13:00", "10", "", "");
        assertEquals(timetable.size(), 6);
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            INPUT_1 + ";" + EXPECTED_1,
    })
    public void timetableShouldNotBuildWhenDayStartTimeIsAfterDayEndTime(
            String ignored1,
            String ignored2,
            String unitString,
            String breakStartsString,
            String breakDurationsString,
            String ignored3
    ) {
        assertThrows(IllegalArgumentException.class, () -> buildTimetable(
                "15:00",
                "10:00",
                unitString,
                breakStartsString,
                breakDurationsString
        ));
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            INPUT_1 + ";" + EXPECTED_1,
    })
    public void timetableShouldNotBuildWhenWorkingUnitDurationIsZero(
            String dayStartString,
            String dayEndString,
            String ignored1,
            String breakStartsString,
            String breakDurationsString,
            String ignored2
    ) {
        assertThrows(IllegalArgumentException.class, () -> buildTimetable(
                dayStartString,
                dayEndString,
                "0",
                breakStartsString,
                breakDurationsString
        ));
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            INPUT_1 + ";" + EXPECTED_1,
    })
    public void timetableShouldNotBuildWhenBreakStartsCountIsNotEqualToBreakDurationsCount(
            String dayStartString,
            String dayEndString,
            String unitString,
            String breakStartsString,
            String breakDurationsString,
            String ignored2
    ) {
        assertThrows(IllegalArgumentException.class, () -> buildTimetable(
                dayStartString,
                dayEndString,
                unitString,
                breakStartsString,
                Arrays.stream(breakDurationsString.split(",")).skip(1).collect(joining(","))
        ));
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            INPUT_1 + ";" + EXPECTED_1,
    })
    public void timetableShouldNotBuildWhenBreakStartIsBeforeTheDayStart(
            String dayStartString,
            String dayEndString,
            String unitString,
            String breakStartsString,
            String breakDurationsString,
            String ignored2
    ) {
        assertThrows(IllegalArgumentException.class, () -> buildTimetable(
                dayStartString,
                dayEndString,
                unitString,
                breakStartsString.replaceFirst(
                        "^\\d{2}:\\d{2}",
                        LocalTime.parse(dayStartString)
                                .minus(Duration.ofMinutes(1))
                                .toString()
                ),
                breakDurationsString
        ));
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            INPUT_1 + ";" + EXPECTED_1,
    })
    public void timetableShouldNotBuildWhenBreakStartIsAfterTheDayStart(
            String dayStartString,
            String dayEndString,
            String unitString,
            String breakStartsString,
            String breakDurationsString,
            String ignored2
    ) {
        assertThrows(IllegalArgumentException.class, () -> buildTimetable(
                dayStartString,
                dayEndString,
                unitString,
                breakStartsString.replaceFirst(
                        "^\\d{2}:\\d{2}",
                        LocalTime.parse(dayEndString)
                                .plus(Duration.ofMinutes(1))
                                .toString()
                ),
                breakDurationsString
        ));
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            INPUT_1 + ";" + EXPECTED_1,
    })
    public void timetableShouldNotBuildWhenBreakEndIsAfterTheDayEnd(
            String dayStartString,
            String dayEndString,
            String unitString,
            String breakStartsString,
            String breakDurationsString,
            String ignored2
    ) {
        assertThrows(IllegalArgumentException.class, () -> buildTimetable(
                dayStartString,
                dayEndString,
                unitString,
                breakStartsString.replaceFirst(
                        "^\\d{2}:\\d{2}",
                        LocalTime.parse(dayEndString)
                                .minus(Duration.ofMinutes(1))
                                .toString()
                ),
                breakDurationsString.replaceFirst("^\\d+", "20")
        ));
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            INPUT_1 + ";" + EXPECTED_1,
    })
    public void timetableShouldNotBuildWhenAnyBreakDurationIsZero(
            String dayStartString,
            String dayEndString,
            String unitString,
            String breakStartsString,
            String breakDurationsString,
            String ignored2
    ) {
        assertThrows(IllegalArgumentException.class, () -> buildTimetable(
                dayStartString,
                dayEndString,
                unitString,
                breakStartsString,
                breakDurationsString.replaceFirst("^\\d+", "0")
        ));
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            INPUT_1 + ";" + EXPECTED_1,
    })
    public void timetableShouldNotBuildWhenAnyBreakDurationHasInvalidFormat(
            String dayStartString,
            String dayEndString,
            String unitString,
            String breakStartsString,
            String breakDurationsString,
            String ignored2
    ) {
        assertThrows(IllegalArgumentException.class, () -> buildTimetable(
                dayStartString,
                dayEndString,
                unitString,
                breakStartsString,
                breakDurationsString.replaceFirst("^\\d+", "invalid")
        ));
    }
}