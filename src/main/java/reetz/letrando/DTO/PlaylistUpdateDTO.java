package reetz.letrando.DTO;

import java.util.List;

public record PlaylistUpdateDTO(
        String name,
        List<String> musicIds
) {}