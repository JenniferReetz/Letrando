package reetz.letrando.DTO;

public record MusicDTO(
        String id,
        String name,
        String album,
        String artist
) {
    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String album() {
        return album;
    }

    @Override
    public String artist() {
        return artist;
    }
}