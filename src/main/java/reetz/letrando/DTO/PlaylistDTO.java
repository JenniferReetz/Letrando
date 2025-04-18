package reetz.letrando.DTO;

import java.util.List;
//entrada por enquanto
public record PlaylistDTO(
        Long id,
        String name,
        List<String> musicIds
) {}