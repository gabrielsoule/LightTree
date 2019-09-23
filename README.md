# LightTree

Powers the lights in my tree. Though there is a very small amount of code specifically tuned to the tree in the backyard, it should work with any WS2811/2812 LED strips plugged into a fadecandy chip. 

To install and use, you'll need to add the libraries for Processing and Minim to your project since they aren't included in the repo and I'm not hooked up to Maven.

To add a new effect, extend LightEffect.class and fill in the appropiate methods. Study the existing effects as examples.