package chylex.bettersprinting.client.player;
import chylex.bettersprinting.client.ClientModManager;
import chylex.bettersprinting.client.input.ToggleTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.util.MovementInput;

final class MovementController{
	private final Minecraft mc;
	private final MovementInput movementInput;
	
	private final ToggleTracker sprintToggle;
	private final ToggleTracker sneakToggle;
	private boolean restoreSneakToggle;
	
	public MovementController(MovementInput movementInput){
		this.mc = Minecraft.getInstance();
		this.movementInput = movementInput;
		
		this.sprintToggle = new ToggleTracker(ClientModManager.keyBindSprintToggle, ClientModManager.keyBindSprintHold);
		this.sneakToggle = new ToggleTracker(ClientModManager.keyBindSneakToggle, mc.gameSettings.keyBindSneak);
	}
	
	// UPDATE | Ensure first parameter of MovementInputFromOptions.func_217607_a still behaves like forced sneak | 1.14.4
	public void update(boolean slowMovement, boolean isSpectator){
		sprintToggle.update();
		sneakToggle.update();
		
		if (movementInput.sneak && sneakToggle.isToggled && mc.currentScreen != null && !(mc.currentScreen instanceof DeathScreen)){
			restoreSneakToggle = true;
			sneakToggle.isToggled = false;
		}
		
		if (restoreSneakToggle && mc.currentScreen == null){
			sneakToggle.isToggled = true;
			restoreSneakToggle = false;
		}
		
		movementInput.tick(slowMovement || sneakToggle.isToggled, isSpectator);
		movementInput.sneak |= sneakToggle.isToggled;
	}
	
	public boolean isSprintToggled(){
		return sprintToggle.isToggled;
	}
	
	public boolean isMovingAnywhere(){
		return Math.abs(movementInput.moveForward) >= 1E-5F || Math.abs(movementInput.moveStrafe) >= 1E-5F;
	}
}
