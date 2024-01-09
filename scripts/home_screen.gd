extends PanelContainer

@onready var user = get_node("/root/User")

# Called when the node enters the scene tree for the first time.
func _ready():
	$VerticalLayout/Infos/InfosLayout/FirstRow/Username.text = user.username
	$VerticalLayout/Infos/InfosLayout/SecondRow/Gold.text = str(user.gold)
	$VerticalLayout/Infos/InfosLayout/SecondRow/Crystals.text = str(user.crystals)


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta):
	pass
