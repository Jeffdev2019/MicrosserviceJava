package br.com.victor.awsproject1.model;

import jakarta.persistence.*;

@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"code"})
        }
)
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ID;
    @Column(length = 32, nullable = false)
    private String name;
    @Column(length = 24, nullable = false)
    private String model;
    @Column(length = 8, nullable = false)
    private String code;

    @Column(length = 20, nullable = false)
    private String color;
    private float price;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
