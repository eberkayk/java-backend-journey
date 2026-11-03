package w5d2;

import java.util.*;
import java.util.stream.Collectors;

public class AttendanceLog {
    private final List<Visit> visits = new ArrayList<>();

    public void add(Visit visit) {
        if (visit == null) {
            throw new IllegalArgumentException("Visit must not be null");
        }
        visits.add(visit);
    }

    public int size() {
        return visits.size();
    }

    public Map<String, Integer> visitsPerPerson() {
        Map<String, Integer> map = new TreeMap<>();
        for (Visit v : visits) {
            map.put(v.person(), map.getOrDefault(v.person(), 0) + 1);
        }
        return map;
    }

    public Set<String> attendeesOf(String event) {
        Set<String> attendeesOf = new LinkedHashSet<>();
        for (Visit v : visits) {
            if (Objects.equals(v.event(), event)) {
                attendeesOf.add(v.person());
            }
        }
        return attendeesOf;
    }

    public Map<String, Set<String>> eventsByPerson() {
        Map<String, Set<String>> map = new TreeMap<>();
        for (Visit v : visits) {
            if (!map.containsKey(v.person())) {
                map.put(v.person(), new TreeSet<>());
            }
            map.get(v.person()).add(v.event());
        }
        return map;
    }

    public Set<String> commonAttendees(String event1, String event2) {
        Set<String> set1 = attendeesOf(event1);
        Set<String> set2 = attendeesOf(event2);
        Set<String> result = new TreeSet<>();
        for (String s : set1) {
            if (set2.contains(s)) {
                result.add(s);
            }
        }
        return result;
    }

    public Set<String> onlyIn(String event1, String event2) {
        Set<String> set1 = attendeesOf(event1);
        Set<String> set2 = attendeesOf(event2);
        Set<String> result = new TreeSet<>();
        for (String s : set1) {
            if (!set2.contains(s)) {
                result.add(s);
            }
        }
        return result;
    }

    public Optional<String> mostActive() {
        Map<String, Integer> map = visitsPerPerson();
        if (map.isEmpty()) {
            return Optional.empty();
        }
        String best = null;
        int bestCount = 0;
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            if (e.getValue() > bestCount) {
                bestCount = e.getValue();
                best = e.getKey();
            }
        }
        return Optional.of(best);

    }

    public Map<Integer, List<String>> peopleByVisitCount() {
        Map<String, Integer> map = visitsPerPerson();
        Map<Integer, List<String>> result = new TreeMap<>();
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            if (!result.containsKey(e.getValue())) {
                result.put(e.getValue(), new ArrayList<>());
            }
            result.get(e.getValue()).add(e.getKey());
        }
        return result;
    }
}
