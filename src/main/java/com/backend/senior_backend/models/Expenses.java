package com.backend.senior_backend.models;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "Expense")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Expenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "user_phone", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "group_id", referencedColumnName = "group_id"),
        @JoinColumn(name = "category_name", referencedColumnName = "name")
    })
    private Categories category;
}
