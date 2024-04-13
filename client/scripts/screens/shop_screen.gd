extends PanelContainer

@onready var daily_cards = $Scroll/VerticalLayout/DailyCards
@onready var chests = $Scroll/VerticalLayout/Chests
@onready var crystals = $Scroll/VerticalLayout/Crystals
@onready var gold = $Scroll/VerticalLayout/Gold

func _ready():
	setup_shop()
	pass

func setup_shop():
	for i in range(6):
		var new_card = ShopCard.new_card("Nome", "Raridade", Color.AQUA, "res://assets/placeholders/Boitata.jpg", 100)
		daily_cards.add_child(new_card)
	for i in range(3):
		var new_card = ShopCard.new_card("Nome", "Raridade", Color.AQUA, "res://assets/placeholders/chest.png", 100)
		chests.add_child(new_card)
	for i in range(3):
		var new_card = ShopCard.new_card("Nome", "Raridade", Color.AQUA, "res://assets/placeholders/crystal.png", 100)
		crystals.add_child(new_card)
	for i in range(3):
		var new_card = ShopCard.new_card("Nome", "Raridade", Color.AQUA, "res://assets/placeholders/gold.png", 100)
		gold.add_child(new_card)
