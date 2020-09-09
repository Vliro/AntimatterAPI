package muramasa.antimatter.cover;

public class CoverNone extends Cover {

    @Override
    public String getId() {
        return "none";
    }

    @Override
    public boolean hasTexture() {
        return false;
    }
}
