About
========

Revolution bar optimiser: given X list of abilities, what's the best bar you can get with it?

See also http://runescape.wikia.com/wiki/Revolution/Bars

Requires Apache Commons CLI - https://commons.apache.org/proper/commons-cli/ (developed on 1.2 but probably works on any after that).


Usage
========

For simple tests, run Optimiser2.class from command line (or within an IDE); edit the main method and recompile (it is full of examples already).

For more sophisticated usage, run CmdOptimiser (the class, or the provided jar in releases) on the command line with options:

**Modes**

* `-t <bar>` to find the AADPT of that bar (comma-separated list of abils)
* `-b <abils>` to optimise for a single bar (comma-separated list of abils)
* `-f <file>` to bulk optimise that file (where the file is a newline-separated list of bars) - outputs to file `-o`
* `-page <file>` to optimise and output a wikipage - outputs to file `-o`

**Options**

* `-o <file>` output file (for `-f` and `-page` modes)
* `-stun` turn on stuns
* `-force <abil>` forcibly include this ability in the bar(s) when optimising
* `-tick <int>` number of ticks to optimise for
* `-max <int>` maximum number of abilities in the bar
* `-iter <int>` maximum number of permutations when searching for an optimised bar

**Pages**

When running in page mode, the page file should be normal wikitext for the most part. Commands to the optimiser begin a line with `@@` and are case insensitive. All parameters are **tab separated**.

* `@@nl` - add a new line (i.e. do nothing)
* `@@SET` sets a specified var - outputs nothing to the page (the linebreak is removed). All bars optimised after changing a var use that value - so if you only want it for one bar, make sure to revert it once you're done.
  * `@@SET	stuns	<boolean>` turn on/off stuns
  * `@@SET slayer	<boolean>` turn on/off slayer
  * `@@SET	ticks	<int>` set the number of ticks to iterate for
  * `@@SET	iter	<int>` set the maximum number of permutations to try
  * `@@SET	max	<int>` set the maximum number of abilities
  * `@@SET	force	<ability>` force the ability to be used in each bar
  * `@@SET	force	off` don't force any ability
* `@@OPTIMISE	<notes>	<class>	<handedness>	<abilities>` output an optimised bar using the given abilities in {{Revolution AADPT row}} (or {{Revolution AADPT NA}} if it doesn't make a bar). Handedness, class, and notes are passed unmodified to the output.


TODO
=====

**Functionality**

Update for revolution++

* Add adrenaline
* Add thresholds and ultimates to config
* Add buffs - damage and crit-adren
* Improve permutation generator, since adding the few extra threshold/ultimate abilities is causing a combinatorial catastrophe
* Probably missing stuff

Other

* Add auto-attacks (and 4taa)
* Add perks
* Add rings
* Add potion
* Etc

**Modes**

Full rewrite - in package OptimiserPrime I was beginning work on a full rewrite of the optimiser code, including moving ability definitions to JSON (abilities.json in resources). Haven't really had time to work on it.

[old] BarDB -  I believe I was aiming to pre-generate every possible combination of abilities so that it could be added as a javascript calculator on the wiki. Javascipt is very bad at handling permutations so with pregenerated bars it would just be a lookup operation - select X abilities, return the top 5 (or just the top 1 or whatever) bars that use those abilities. Obviously I didn't get far.

[old] GUI - I wanted to make a nice GUI frontend but I also didn't get far with it. That's what all the images in resources are.

Bestiary
========

Bestiary scraper. Mostly here because I was lazy and didn't make a new project. Requires GSON - https://github.com/google/gson
