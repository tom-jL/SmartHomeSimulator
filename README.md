# Home Automation Simulator

GETTING STARTED:

Requires at least JDK 8 (Have seen Spinner not available in outdated JDKs)

Default house can be loaded from the file menu. This is parsed from the sprites.xml file.
img/sprites should be available, should be committed.

You can click on the different sprites to check their status, delete them from view,
run or schedule an action. Some sprites are "smart" some aren't.

If you ever feel like creating your own house there are palettes available to paint your own!
Palettes are loaded from the palette.xml file, you could create your own furniture/fixtures here if you like.

Charts are showing usage and weather stats every 10 minutes.
Player can be controlled with the WASD keys. There is a proximity sensor for all smart enabled sprites.
This will cause some sprites to turn on/animate based on proximity.

Running the simulator from the Simulation menu will start a new day.
Start/stop will resume/pause the simulation.

For testing you need to set VM options in Edit Configurations with switch "-ea" (Enable Asserts)


KNOWN ISSUES:

deleting/adding sprites may cause thread concurrency issues.
Help menu has no functionality at this stage.
Window is not resizable at this stage.
Proximity and actions may conflict causing bad behaviour.

