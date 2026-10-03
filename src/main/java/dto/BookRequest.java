package dto;

public record BookRequest(
        Long id,
        String title,
        String author,
        int price
) {

}
