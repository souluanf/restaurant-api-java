package dev.luanfernandes.restaurant.domain.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import dev.luanfernandes.restaurant.domain.enums.ProductCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(id, product.id)
                .append(name, product.name)
                .append(price, product.price)
                .append(category, product.category)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(id)
                .append(name)
                .append(price)
                .append(category)
                .toHashCode();
    }
}
