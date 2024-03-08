class_name ShopCard
extends PanelContainer

const shop_card_scene: PackedScene = preload("res://scenes/main_menu/shop/shop_card.tscn")

var card_name : String
# optional
var card_description : String
# optional
var card_description_color : Color
var card_image : String
var card_cost : int

# Buying with gold or crystals
#var card_currency 

func _ready():
	$VBoxContainer/CardName.text = card_name
	$VBoxContainer/CardDescription.text = card_description
	$VBoxContainer/CardDescription.add_theme_color_override("font_color", card_description_color)
	var loaded_image = load(card_image)
	$VBoxContainer/CardImage/CardTexture.texture = loaded_image
	$VBoxContainer/CardCost.text = str(card_cost)

static func new_card(name: String, description: String, color : Color, image : String, cost: int) -> ShopCard:
	var new_card: ShopCard = shop_card_scene.instantiate()
	new_card.card_name = name
	new_card.card_description = description
	new_card.card_description_color = color
	new_card.card_image = image
	new_card.card_cost = cost
	return new_card
