package TienToan.example.Cinema.Entity;


import TienToan.example.Cinema.DTO.request.MovieSearchRequest;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MovieSpecification {

    public static Specification<Movie> filter(MovieSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(request.title())) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + request.title().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(request.genre())) {
                predicates.add(cb.equal(root.get("genre"), request.genre()));
            }

            if (StringUtils.hasText(request.city()) || request.date() != null || Boolean.TRUE.equals(request.isShowing())) {

                Join<Movie, Schedule> schedules = root.join("schedules", JoinType.INNER);

                if (StringUtils.hasText(request.city())) {
                    predicates.add(cb.equal(schedules.get("room").get("cinema").get("city"), request.city()));
                }

                if (request.date() != null) {
                    LocalDateTime startOfDay = request.date().atStartOfDay();
                    LocalDateTime endOfDay = request.date().atTime(LocalTime.MAX);
                    predicates.add(cb.between(schedules.get("startTime"), startOfDay, endOfDay));
                }

                predicates.add(cb.greaterThan(schedules.get("startTime"), LocalDateTime.now()));

                query.distinct(true);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
