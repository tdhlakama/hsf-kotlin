package hsfweb.controller.endpoints;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by scott on 22/05/2017.
 */
@MappedSuperclass
public class BaseModel implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Transient
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
