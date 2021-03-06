package com.ryanlea.fix.chronicle.spec;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.List;

public class ComponentDefinition implements EntityDefinition {

    private final String name;

    private final List<FieldReference> fields = new ArrayList<>();

    private final List<ComponentReference> components = new ArrayList<>();

    private final TIntObjectMap<FieldReference> fieldReferencesByNumber = new TIntObjectHashMap<>();

    private final TIntObjectMap<FieldDefinition> fieldsByNumber = new TIntObjectHashMap<>();

    private final TIntObjectMap<ComponentDefinition> componentDefinitionsByName = new TIntObjectHashMap<>();

    private ComponentDefinition[] componentDefinitions;

    private FieldDefinition[] fieldDefinitions;

    public ComponentDefinition(String name) {
        this.name = name;
    }

    @Override
    public void addFieldReference(FieldReference fieldReference) {
        fields.add(fieldReference);
    }

    @Override
    public void addComponentReference(ComponentReference componentReference) {
        components.add(componentReference);
    }

    @Override
    public boolean hasField(int tag) {
        return fieldsByNumber.containsKey(tag);
    }

    @Override
    public FieldDefinition[] getFieldDefinitions() {
        return fieldDefinitions;
    }

    @Override
    public GroupDefinition getGroupDefinition(int tag) {
        return (GroupDefinition) fieldReferencesByNumber.get(tag);
    }

    @Override
    public ComponentDefinition[] getComponentDefinitions() {
        return componentDefinitions;
    }

    @Override
    public ComponentDefinition getComponentDefinition(String name) {
        return componentDefinitionsByName.get(name.hashCode());
    }

    public Iterable<? extends FieldReference> getFieldReferences() {
        return fields;
    }

    @Override
    public boolean embedsField(int tag) {
        for (int i = 0; i < componentDefinitions.length; i++) {
            final ComponentDefinition componentDefinition = componentDefinitions[i];
            if (componentDefinition.hasField(tag) || componentDefinition.embedsField(tag)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void init(FixSpec fixSpec) {
        fieldDefinitions = new FieldDefinition[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            final FieldReference fieldReference = fields.get(i);
            final FieldDefinition fieldDefinition = fixSpec.getFieldDefinition(fieldReference);
            fieldsByNumber.put(fieldDefinition.getNumber(), fieldDefinition);
            fieldDefinitions[i] = fieldDefinition;
            fieldReferencesByNumber.put(fieldDefinition.getNumber(), fieldReference);
        }

        componentDefinitions = new ComponentDefinition[components.size()];
        for (int i = 0; i < components.size(); i++) {
            final int idx = i;
            ComponentReference componentReference = components.get(i);
            ComponentDefinition componentDefinition = fixSpec.getComponentDefinition(componentReference);
            if (componentDefinition == null) {
                fixSpec.subscribe(componentReference.getName(), new ComponentDefinitionListener() {
                    @Override
                    public void registered(ComponentDefinition componentDefinition) {
                        componentDefinitions[idx] = componentDefinition;
                        componentDefinitionsByName.put(componentDefinition.getName().hashCode(), componentDefinition);
                    }
                });
            } else {
                componentDefinitions[i] = componentDefinition;
                componentDefinitionsByName.put(componentDefinition.getName().hashCode(), componentDefinition);
            }
        }
    }

}
