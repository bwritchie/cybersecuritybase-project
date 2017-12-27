package sec.project.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sec.project.domain.Note;

/**
 * Repository for employee Note entities
 * 
 * @author BenR
 */
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUsername(String username);
}
