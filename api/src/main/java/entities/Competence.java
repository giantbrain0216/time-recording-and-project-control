package entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.validation.constraints.NotNull;

@DatabaseTable(tableName = "Competences")
public class Competence {

    @DatabaseField(columnName = "ID", id = true)
    @NotNull
    private Integer id;

    @DatabaseField(columnName = "Name")
    @NotNull
    private String name;

    public Competence() {
    }

    public Competence(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
