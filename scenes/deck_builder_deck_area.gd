extends Node2D

@onready var tm = $TileMap as TileMap

# Called when the node enters the scene tree for the first time.
func _ready():
	var ts = tm.tile_set as TileSet
	print("TILESET:")
	print(ts)
	var v = 4
	for l in range(0,3):
		for x in range(-4,v):
			for y in range(-4,v):
				var cell = tm.get_cell_tile_data(l,Vector2i(x,y))
				print(cell)
	#print()
	#pass # Replace with function body.

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta):
	pass
