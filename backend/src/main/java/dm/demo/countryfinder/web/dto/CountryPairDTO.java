package dm.demo.countryfinder.web.dto;

import lombok.*;

import java.util.Objects;

/**
 * Unordered pair of iso codes.
 */
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class CountryPairDTO {
    private String left;
    private String right;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CountryPairDTO)) {
            return false;
        }

        CountryPairDTO dto = (CountryPairDTO) obj;
        return (Objects.equals(this.left, dto.getLeft()) && Objects.equals(this.right, dto.getRight()))
                || (Objects.equals(this.left, dto.getRight()) && Objects.equals(this.right, dto.getLeft()));
    }

    @Override
    public int hashCode() {
        int leftHashCode = Objects.nonNull(this.left) ? this.left.hashCode() : 0;
        int rightHashCode = Objects.nonNull(this.right) ? this.right.hashCode() : 0;

        int result = 1;
        result = 31 * result + Math.min(leftHashCode, rightHashCode);
        result = 31 * result + Math.max(leftHashCode, rightHashCode);
        return result;
    }
}
