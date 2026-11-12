package w5d3;

import java.time.LocalDate;
import java.util.*;

public class TaskBoard {
    private final List<Task> tasks = new ArrayList<>();

    public void add(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task must not be null");
        }

        if (tasks.stream().map(Task::title).anyMatch(n -> Objects.equals(n, task.title()))) {
            throw new IllegalStateException("Duplicate title: " + task.title());
        }

        tasks.add(task);
    }

    public int size() {
        return tasks.size();
    }

    public List<Task> sortedNatural() {
        return tasks.stream().sorted(Task::compareTo).toList();
    }

    public List<Task> byPriority() {
        return tasks.stream().sorted(Comparator.comparing(Task::priority).reversed().thenComparing(Task::due).thenComparing(Task::title)).toList();
    }

    public List<String> titlesSortedBy(Comparator<Task> order) {
        return tasks.stream().sorted(order).map(Task::title).toList();
    }

    public int removeOverdue(LocalDate today) {
        int count = 0;
        Iterator<Task> it = tasks.iterator();
        while (it.hasNext()) {
            Task x = it.next();
            if (x.due().isBefore(today)) {
                it.remove();
                count++;
            }
        }
        return count;
    }

    public Optional<Pair<Task, Task>> earliestAndLatest() {
        if (size() == 0) {
            return Optional.empty();
        }

        List<Task> sortedList = tasks.stream().sorted(Comparator.naturalOrder()).toList();
        Task first = sortedList.getFirst();
        Task last = sortedList.getLast();

        return Optional.of(new Pair<>(first, last));
    }

    public Map<Integer, List<String>> titlesByPriority() {
        Map<Integer, List<String>> result = new TreeMap<>(Comparator.reverseOrder());
        for (Task t : tasks) {
            if (!result.containsKey(t.priority())) {
                result.put(t.priority(), new ArrayList<>());
            }
            result.get(t.priority()).add(t.title());
            result.get(t.priority()).sort(Comparator.naturalOrder());
        }
        return result;
    }
}
