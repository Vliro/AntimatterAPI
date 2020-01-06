package muramasa.antimatter.machines;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import muramasa.antimatter.registration.IAntimatterObject;
import muramasa.antimatter.texture.Texture;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;

public class Tier implements IAntimatterObject {

    private static HashMap<String, Tier> TIER_NAME_LOOKUP = new HashMap<>();
    private static Int2ObjectOpenHashMap<Tier> TIER_ID_LOOKUP = new Int2ObjectOpenHashMap<>();

    private static int lastInternalId = 0;

    /** Electricity Tiers **/
    public static Tier ULV = new Tier("ulv", 8, TextFormatting.WHITE); //Tier 1
    public static Tier LV = new Tier("lv", 32, TextFormatting.WHITE); //Tier 2
    public static Tier MV = new Tier("mv", 128, TextFormatting.WHITE); //Tier 3
    public static Tier HV = new Tier("hv", 512, TextFormatting.YELLOW); //Tier 4
    public static Tier EV = new Tier("ev", 2048, TextFormatting.AQUA); //Tier 5
    public static Tier IV = new Tier("iv", 8192, TextFormatting.LIGHT_PURPLE); //Tier 6
    public static Tier LUV = new Tier("luv", 32768, TextFormatting.LIGHT_PURPLE); //Tier 7
    public static Tier ZPM = new Tier("zpm", 131072, TextFormatting.LIGHT_PURPLE); //Tier 8
    public static Tier UV = new Tier("uv", 524288, TextFormatting.LIGHT_PURPLE); //Tier 9
    public static Tier MAX = new Tier("max", 2147483647, TextFormatting.LIGHT_PURPLE); //Tier 15

    /** Special Tiers **/
    //TODO make these Tier 0 and 1?
    public static Tier BRONZE = new Tier("bronze", 0, TextFormatting.WHITE);
    public static Tier STEEL = new Tier("steel", 0, TextFormatting.WHITE);

    private int internalId;
    private String id;
    private long voltage;
    private TextFormatting rarityColor;
    private Texture baseTexture;

    public Tier(String id, long voltage, TextFormatting rarityColor) {
        internalId = lastInternalId++;
        this.id = id;
        this.voltage = voltage;
        this.rarityColor = rarityColor;
        this.baseTexture = new Texture("block/machine/base/" + id);
        TIER_NAME_LOOKUP.put(id, this);
        TIER_ID_LOOKUP.put(internalId, this);
    }

    public int getInternalId() {
        return internalId;
    }

    @Override
    public String getId() {
        return id;
    }

    public long getVoltage() {
        return voltage;
    }

    public TextFormatting getRarityColor() {
        return rarityColor;
    }

    public Texture getBaseTexture() {
        return baseTexture;
    }

    public static int getCount() {
        return TIER_NAME_LOOKUP.size();
    }

    public static Tier[] getSteam() {
        return new Tier[]{BRONZE, STEEL};
    }

    public static Tier[] getStandard() {
        return new Tier[]{LV, MV, HV, EV, IV};
    }

    public static Tier[] getAllElectric() {
        return new Tier[]{ULV, LV, MV, HV, EV, IV, LUV, ZPM, UV, MAX};
    }

    @Nullable
    public static Tier get(String name) {
        return TIER_NAME_LOOKUP.get(name);
    }

    @Nullable
    public static Tier get(int id) {
        return TIER_ID_LOOKUP.get(id);
    }

    public static Tier[] getBasic() {
        return new Tier[]{BRONZE, STEEL, LV, MV, HV, EV, IV};
    }

    public static Tier getMax() {
        return Tier.IV; //TODO update...
    }

    public static Collection<Tier> getAll() {
        return TIER_NAME_LOOKUP.values();
    }

    public static Texture[] getTextures(Tier... tiers) {
        Texture[] textures = new Texture[tiers.length];
        for (int i = 0; i < tiers.length; i++) {
            textures[i] = tiers[i].getBaseTexture();
        }
        return textures;
    }

    public static Texture[] getAllTextures() {
        return getTextures(TIER_NAME_LOOKUP.values().toArray(new Tier[0]));
    }
}