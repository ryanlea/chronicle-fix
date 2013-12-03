package com.ryanlea.fix.chronicle;

import com.ryanlea.fix.chronicle.spec.GroupDefinition;
import org.joda.time.ReadableDateTime;

public abstract class Group {

    private Fields[] fields;

    private int length;

    private final GroupDefinition groupDefinition;

    protected Group(GroupDefinition groupDefinition) {
        this.groupDefinition = groupDefinition;
    }

    protected int length() {
        return length;
    }

    protected CharSequence _string(int idx, int fid) {
        return getFields(idx)._string(fid);
    }

    protected int _int(int idx, int fid) {
        return getFields(idx)._int(fid);
    }

    protected char _char(int idx, int fid) {
        return getFields(idx)._char(fid);
    }

    protected ReadableDateTime _dateTime(int idx, int fid) {
        return getFields(idx)._dateTime(fid);
    }

    protected Number _decimal(int idx, int fid) {
        return getFields(idx)._decimal(fid);
    }

    private Fields getFields(int idx) {
        growIfRequired(idx);
        return fields[idx];
    }

    private void growIfRequired(int idx) {
        if (idx >= fields.length) {
            int previousLength = fields.length;
            // should this be double or larger ... ?
            Fields[] tmp = new Fields[idx + 1];
            System.arraycopy(fields, 0, tmp, 0,  fields.length);
            fields = tmp;

            for (int i = previousLength; i < fields.length; i++) {
                fields[i] = new GroupFields(groupDefinition);
            }
        }
    }

    private static class GroupFields extends Fields {

        protected GroupFields(GroupDefinition groupDefinition) {
            super(groupDefinition.getFieldDefinitions());
        }
    }
}
