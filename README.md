![alt text](https://github.com/gabrielsoule/LightTree/blob/master/screenshot.jpg)

# LightTree

Powers the lights in my tree. Though there is a very small amount of code specifically tuned to the tree in the backyard, it should work with any WS2811/2812 LED strips plugged into a Fadecandy chip. It runs on Processing (processing.org) to power the visualizer UI. You can run the visualizer without connecting to a Fadecandy server, though obviously it doesn't look as cool without the actual lights...

The project works out of the box with Maven. Clone it from GitHub and give it a whirl. Processing is still stuck on Java 1.8, so make sure your environment is configured to use that. 

Everything is likely to break and change since this project is still in active development. Watch out!

## Implemented
- User generated beat detection
- Connect to a FC server over network
- Simple client-side visualizer
- LightEffect API (mostly, still WIP)
- Configurable effects via config file

##NYI
- Keyboard hooks for LightEffect API
- Configurable hardware light mapping (unless you're really careful and smart (unlike me) your lights' hardware addresses might not match up with their order in real life (as they're strung up on your ceiling/tree/whatever)... currently a fix for this is hardcoded for my setup specifically in my yard... obviously this is bad...)
- onSleep/onWake methods for inactive effects, allow for instant effects that do one thing (like a big flash!!) and then put themselves to sleep
- On-the-fly color modification via keyboard input
- Effect layering (currently only one active effect at a time, in future, they will be layerable)
- Simple wrapper API for minim information that doesn't require using FFT or signal processing directly (e.g. getBassLoudness, getTrebleLoudness, etc something like that) 
- Fancy UI that looks sick

##Future plans?
- Load effects from external jar files, so that you don't have to fork the source and make your own version... kinda a plugin model? 


## Creating an effect

To create an effect, extend LightEffect.class and make sure your class is in the com.gabrielsoule.lighttree.effects package. Then, override the appropiate methods as required. There are some existing LightEffects that serve as good examples.
### `draw()`
This is called every frame. In this method you can use `setLight` to actually draw your effect onto the lights. You'll probably want to call `flushColors()` at the start of each draw call, since the draw buffer is not cleared by default every frame. Frame rate is not necessarily fixed, so make sure you use the the delta time `1 / (float) p.FRAME_RATE` instead of advancing your effects a fixed amount per frame, 

### `setup()`
This is called when your effect is first loaded at startup. Use for initialization of variables and such. It is not called when an effect is re-activated by the user, instead, 

### `EffectConfig`
Each effect has an associated EffectConfig member which holds configuration information from `config.yml`, such as colors (which are dynamic) and other options your effect might use (speed, brightness, idk whatever). It is unwise to cache these values on startup since they may change with each invocation of the `get` methods, depending on user input while the program is active. 

### `keyboardListener`
Not yet implemented properly for effects. No touch
