package gunjoon98.scoreboard.domain.service;

import gunjoon98.scoreboard.domain.repository.JdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final JdbcRepository jdbcRepository;


}
