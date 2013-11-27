package com.ryanlea.fix.chronicle.spec;

import java.util.ArrayList;
import java.util.List;

public class MessageDefinition implements EntityDefinition {

    private final List<FieldReference> fields = new ArrayList<FieldReference>();

    private final List<GroupDefinition> groups = new ArrayList<GroupDefinition>();

    public void addFieldReference(FieldReference fieldReference) {
        fields.add(fieldReference);
    }

    public void addGroupDefinition(GroupDefinition groupDefinition) {
        groups.add(groupDefinition);
    }

}
