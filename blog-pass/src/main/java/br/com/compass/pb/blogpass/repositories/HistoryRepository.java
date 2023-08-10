package br.com.compass.pb.blogpass.repositories;

import br.com.compass.pb.blogpass.entities.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<StatusHistory, Long> {
}
