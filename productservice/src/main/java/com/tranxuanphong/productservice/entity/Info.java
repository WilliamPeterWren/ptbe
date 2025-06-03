package com.tranxuanphong.productservice.entity;

import java.util.Objects;

import org.springframework.data.annotation.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data; 
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@NoArgsConstructor 
@AllArgsConstructor 
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data 
public class Info {

    @Id
    String id;
    String name;
    String detail;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Info other = (Info) o;

        return Objects.equals(this.id, other.id) &&
               Objects.equals(this.name, other.name) &&
               Objects.equals(this.detail, other.detail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, detail);
    }

    @Override
    public String toString() {
        return "Info{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", detail='" + detail + '\'' +
               '}';
    }
}