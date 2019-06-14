package chylex.bettersprinting.client;
import chylex.bettersprinting.BetterSprintingConfig;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientSettings{
	public static int keyCodeSprintHold = 29;
	public static int keyCodeSprintToggle = 34;
	public static int keyCodeSneakToggle = 21;
	public static int keyCodeOptionsMenu = 24;
	
	public static KeyModifier keyModSprintHold = KeyModifier.NONE;
	public static KeyModifier keyModSprintToggle = KeyModifier.NONE;
	public static KeyModifier keyModSneakToggle = KeyModifier.NONE;
	public static KeyModifier keyModOptionsMenu = KeyModifier.NONE;
	
	public static int flySpeedBoost = 3;
	public static boolean enableDoubleTap = false;
	public static boolean enableAllDirs = false;
	public static boolean disableMod = false;
	
	public static boolean enableUpdateNotifications = true;
	public static boolean enableBuildCheck = true;
	
	public static void reload(BetterSprintingConfig config){
		config.setCategory("client");
		
		keyCodeSprintHold = config.get("keySprintHold", keyCodeSprintHold).setShowInGui(false).getInt();
		keyCodeSprintToggle = config.get("keySprintToggle", keyCodeSprintToggle).setShowInGui(false).getInt();
		keyCodeSneakToggle = config.get("keySneakToggle", keyCodeSneakToggle).setShowInGui(false).getInt();
		keyCodeOptionsMenu = config.get("keyOptionsMenu", keyCodeOptionsMenu).setShowInGui(false).getInt();
		
		keyModSprintHold = KeyModifier.valueOf(config.get("keyModSprintHold", keyModSprintHold.name()).setShowInGui(false).getString());
		keyModSprintToggle = KeyModifier.valueOf(config.get("keyModSprintToggle", keyModSprintToggle.name()).setShowInGui(false).getString());
		keyModSneakToggle = KeyModifier.valueOf(config.get("keyModSneakToggle", keyModSneakToggle.name()).setShowInGui(false).getString());
		keyModOptionsMenu = KeyModifier.valueOf(config.get("keyModOptionsMenu", keyModOptionsMenu.name()).setShowInGui(false).getString());
		
		flySpeedBoost = MathHelper.clamp(config.get("flySpeedBoost", flySpeedBoost).setShowInGui(false).getInt(), 0, 7);
		enableDoubleTap = config.get("enableDoubleTap", enableDoubleTap).setShowInGui(false).getBoolean();
		enableAllDirs = config.get("enableAllDirs", enableAllDirs).setShowInGui(false).getBoolean();
		disableMod = config.get("disableMod", disableMod).setShowInGui(false).getBoolean();
		
		enableUpdateNotifications = config.get("enableUpdateNotifications", enableUpdateNotifications, I18n.format("bs.config.notifications")).getBoolean();
		enableBuildCheck = config.get("enableBuildCheck", enableBuildCheck, I18n.format("bs.config.buildCheck")).getBoolean();
		
		ClientModManager.keyBindSprintHold.setKeyModifierAndCode(keyModSprintHold, keyCodeSprintHold);
		ClientModManager.keyBindSprintToggle.setKeyModifierAndCode(keyModSprintToggle, keyCodeSprintToggle);
		ClientModManager.keyBindSneakToggle.setKeyModifierAndCode(keyModSneakToggle, keyCodeSneakToggle);
		ClientModManager.keyBindOptionsMenu.setKeyModifierAndCode(keyModOptionsMenu, keyCodeOptionsMenu);
		
		config.update();
	}
	
	public static void update(BetterSprintingConfig config){
		config.setCategory("client");
		
		config.set("keySprintHold", keyCodeSprintHold);
		config.set("keySprintToggle", keyCodeSprintToggle);
		config.set("keySneakToggle", keyCodeSneakToggle);
		config.set("keyOptionsMenu", keyCodeOptionsMenu);
		
		config.set("keyModSprintHold", keyModSprintHold.name());
		config.set("keyModSprintToggle", keyModSprintToggle.name());
		config.set("keyModSneakToggle", keyModSneakToggle.name());
		config.set("keyModOptionsMenu", keyModOptionsMenu.name());
		
		config.set("flySpeedBoost", flySpeedBoost);
		config.set("enableDoubleTap", enableDoubleTap);
		config.set("enableAllDirs", enableAllDirs);
		config.set("disableMod", disableMod);
		
		config.set("enableUpdateNotifications", enableUpdateNotifications);
		config.set("enableBuildCheck", enableBuildCheck);
		
		config.update();
	}
}
