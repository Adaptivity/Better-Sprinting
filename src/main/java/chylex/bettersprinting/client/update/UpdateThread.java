package chylex.bettersprinting.client.update;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import chylex.bettersprinting.BetterSprintingMod;
import chylex.bettersprinting.client.ClientSettings;
import chylex.bettersprinting.system.Log;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class UpdateThread extends Thread{
	private static final String url = "https://raw.githubusercontent.com/chylex/Better-Sprinting/master/UpdateNotificationData.txt";
	
	private final String modVersion;
	private final String mcVersion;
	
	public UpdateThread(String modVersion){
		this.modVersion = modVersion;
		this.mcVersion = MinecraftForge.MC_VERSION;
		setPriority(MIN_PRIORITY);
		setDaemon(true);
	}
	
	@Override
	public void run(){
		try{
			String line;
			StringBuilder build = new StringBuilder();
			
			BufferedReader read = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			while((line = read.readLine()) != null)build.append(line).append('\n');
			read.close();
			
			JsonElement root = new JsonParser().parse(build.toString());
			List<VersionEntry> versionList = Lists.newArrayList();
			VersionEntry newestVersion = null, newestVersionForCurrentMC = null;
			int counter = -1;
			String buildId = "";
			boolean isInDev = true;
			
			String downloadURL = "http://tinyurl.com/better-sprinting-mod";
			
			Log.debug("Detecting Better Sprinting updates...");
			
			for(Entry<String,JsonElement> entry:root.getAsJsonObject().entrySet()){
				if (entry.getKey().charAt(0) == '~'){
					if (entry.getKey().substring(1).equals("URL"))downloadURL = entry.getValue().getAsString();
				}
				else versionList.add(new VersionEntry(entry.getKey(),entry.getValue().getAsJsonObject()));
			}
			
			Collections.sort(versionList);
			
			for(VersionEntry version:versionList){
				Log.debug("Reading update data: $0",version.versionIdentifier);

				if (newestVersion == null)newestVersion = version;
				
				if (version.isSupportedByMC(mcVersion)){
					if (newestVersionForCurrentMC == null)newestVersionForCurrentMC = version;
					++counter;
				}
				
				if (version.modVersion.equals(modVersion)){
					isInDev = false;
					buildId = version.buildId;
					UpdateNotificationManager.refreshUpdateData(version);
					break;
				}
			}
			
			if (isInDev){
				Log.debug("In-dev version used, notifications disabled.");
				return;
			}
			else Log.debug("Done.");
			
			if (!buildId.isEmpty() && !buildId.equals(BetterSprintingMod.buildId)){
				StringBuilder message = new StringBuilder()
					.append(EnumChatFormatting.LIGHT_PURPLE).append(" [Better Sprinting ").append(modVersion).append("]").append(EnumChatFormatting.RESET)
					.append("\n Caution, you are using a broken build that can cause critical crashes! Please, redownload the mod, or update it if there is an update available.")
					.append("\n\n ").append(EnumChatFormatting.GRAY).append(downloadURL);
				
				for(String s:message.toString().split("\n"))Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(s));
			}
			else if (counter > 0 && ClientSettings.enableUpdateNotifications){
				StringBuilder message = new StringBuilder()
					.append(EnumChatFormatting.GREEN).append(" [Better Sprinting ").append(modVersion).append("]").append(EnumChatFormatting.RESET)
					.append("\n Found a new version ").append(EnumChatFormatting.GREEN).append(newestVersionForCurrentMC.modVersionName).append(EnumChatFormatting.RESET)
					.append(" for Minecraft ").append(mcVersion).append(", released ").append(newestVersionForCurrentMC.releaseDate)
					.append(". You are currently ").append(counter).append(" version").append(counter == 1 ? "" : "s").append(" behind.");
				
				if (newestVersion != newestVersionForCurrentMC){
					message.append("\n\n There is also an update ").append(EnumChatFormatting.GREEN).append(newestVersion.modVersion).append(EnumChatFormatting.RESET)
						   .append(" for Minecraft ").append(CommandBase.joinNiceString(newestVersion.mcVersions)).append('.');
				}
				
				message.append("\n\n ").append(EnumChatFormatting.GRAY).append(downloadURL);
				
				for(String s:message.toString().split("\n"))Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(s));
			}
			else if (newestVersion != newestVersionForCurrentMC && ClientSettings.enableUpdateNotifications){
				StringBuilder message = new StringBuilder()
					.append(EnumChatFormatting.GREEN).append(" [Better Sprinting ").append(modVersion).append("]").append(EnumChatFormatting.RESET)
					.append("\n Found a new version ").append(EnumChatFormatting.GREEN).append(newestVersion.modVersion).append(EnumChatFormatting.RESET)
					.append(" for Minecraft ").append(CommandBase.joinNiceString(newestVersion.mcVersions)).append(", released ").append(newestVersion.releaseDate)
					.append(".");
				
				for(String s:message.toString().split("\n"))Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(s));
			}
		}
		catch(UnknownHostException e){}
		catch(Exception e){
			Log.throwable(e,"Error detecting updates!");
		}
	}
}