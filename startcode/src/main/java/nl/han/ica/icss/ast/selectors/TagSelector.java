package nl.han.ica.icss.ast.selectors;

import nl.han.ica.icss.ast.Selector;

import java.util.Objects;

public class TagSelector extends Selector {
    private String tag;

    public TagSelector(String tag) {
        this.tag = tag;
    }

    public String getNodeLabel() {
        return "TagSelector " + tag;
    }

    @Override
    public String getSelector() {
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TagSelector that = (TagSelector) o;
        return Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {

        return Objects.hash(tag);
    }
}
