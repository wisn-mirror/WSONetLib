package want.com.authtest.bean;

/**
 * Created by wisn on 2017/7/28.
 */

public class Tag {
    public String Name;
    public boolean isCheck;

    public Tag(String name, boolean isCheck) {
        Name = name;
        this.isCheck = isCheck;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        return Name.equals(tag.Name);

    }

    @Override
    public int hashCode() {
        return Name.hashCode();
    }
}
