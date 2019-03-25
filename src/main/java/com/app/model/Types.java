package com.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Set;

@Entity
@SqlResultSetMapping(
        name = "view",
        classes = {
                @ConstructorResult(
                        targetClass = Types.class,
                        columns={
                                @ColumnResult(name = "typeId",type = Integer.class),

                                @ColumnResult(name = "typeName",type = String.class)
                        }
                )
        }
)
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class Types {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer typeId;
    private String typeName;

    @OneToMany(mappedBy = "types",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnoreProperties("types")
    private Set<RecipeTypes> recipeTypes;

    @Override
    public String toString() {
        return "Types{" +
                "typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", recipeTypes=" + recipeTypes +
                '}';
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Set<RecipeTypes> getRecipeTypes() {
        return recipeTypes;
    }

    public void setRecipeTypes(Set<RecipeTypes> recipeTypes) {
        this.recipeTypes = recipeTypes;
    }
}
