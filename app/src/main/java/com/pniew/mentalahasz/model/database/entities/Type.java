package com.pniew.mentalahasz.model.database.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.pniew.mentalahasz.model.repository.AThing;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Entity (indices = {@Index("typeId")})
public class Type implements AThing {
    @PrimaryKey(autoGenerate = true)
    private int typeId;
    private String typeName;
    private String typeFunFact;

    @NotNull
    @Override
    public String toString() {
        return typeName;
    }

    public Type(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeId() {
        return typeId;
    }
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeFunFact() { return typeFunFact; }
    public void setTypeFunFact(String typeFunFact) { this.typeFunFact = typeFunFact; }

    @Override
    public int getId() {
        return getTypeId();
    }

    @Override
    public boolean equals(AThing o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return getTypeId() == type.getTypeId() &&
                getTypeName().equals(type.getTypeName()) &&
                getTypeFunFact().equals(type.getTypeFunFact());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTypeId(), getTypeName(), getTypeFunFact());
    }
}
