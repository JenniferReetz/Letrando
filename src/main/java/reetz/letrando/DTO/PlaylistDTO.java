package reetz.letrando.DTO;

import java.util.List;

public record PlaylistDTO(
        Long id,
        String name,
        Long userId,
        List<String> musicIds
) {}