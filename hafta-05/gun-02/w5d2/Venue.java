package w5d2;

import java.util.*;

public class Venue {
    private final Set<Seat> booked = new HashSet<>();
    private final Map<Seat, String> owners = new HashMap<>();

    public boolean book(Seat seat, String person) {
        if (seat == null) {
            throw new IllegalArgumentException("Seat must not be null");
        }
        if (owners.containsKey(seat)) {
            return false;
        }
        owners.put(seat, person);
        booked.add(seat);
        return true;
    }

    public boolean cancel(Seat seat) {
        if (!owners.containsKey(seat)) {
            return false;
        }
        booked.remove(seat);
        owners.remove(seat);
        return true;
    }

    public boolean isBooked(Seat seat) {
        return owners.containsKey(seat);
    }

    public Optional<String> ownerOf(Seat seat) {
        if (!owners.containsKey(seat)) {
            return Optional.empty();
        } else {
            return Optional.of(owners.get(seat));
        }
    }

    public int bookedCount() {
        return booked.size();
    }

    public Set<Seat> seatsOf(String person) {
        Set<Seat> seats = new HashSet<>();
        for (Map.Entry<Seat, String> e : owners.entrySet()) {
            if (Objects.equals(e.getValue(), person)) {
                seats.add(e.getKey());
            }
        }
        return Set.copyOf(seats);
    }
}
