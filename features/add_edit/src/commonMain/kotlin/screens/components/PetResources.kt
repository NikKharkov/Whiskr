package screens.components

import org.example.whiskr.dto.PetType
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import whiskr.features.add_edit.generated.resources.Res
import whiskr.features.add_edit.generated.resources.ic_cat
import whiskr.features.add_edit.generated.resources.ic_dog
import whiskr.features.add_edit.generated.resources.ic_fish
import whiskr.features.add_edit.generated.resources.ic_hamster
import whiskr.features.add_edit.generated.resources.ic_horse
import whiskr.features.add_edit.generated.resources.ic_parrot
import whiskr.features.add_edit.generated.resources.ic_rabbit
import whiskr.features.add_edit.generated.resources.ic_snake
import whiskr.features.add_edit.generated.resources.ic_spider
import whiskr.features.add_edit.generated.resources.ic_turtle
import whiskr.features.add_edit.generated.resources.pet_cat
import whiskr.features.add_edit.generated.resources.pet_dog
import whiskr.features.add_edit.generated.resources.pet_fish
import whiskr.features.add_edit.generated.resources.pet_hamster
import whiskr.features.add_edit.generated.resources.pet_horse
import whiskr.features.add_edit.generated.resources.pet_parrot
import whiskr.features.add_edit.generated.resources.pet_rabbit
import whiskr.features.add_edit.generated.resources.pet_snake
import whiskr.features.add_edit.generated.resources.pet_spider
import whiskr.features.add_edit.generated.resources.pet_turtle


val PetType.iconRes: DrawableResource
    get() = when (this) {
        PetType.CAT -> Res.drawable.ic_cat
        PetType.DOG -> Res.drawable.ic_dog
        PetType.PARROT -> Res.drawable.ic_parrot
        PetType.HAMSTER -> Res.drawable.ic_hamster
        PetType.RABBIT -> Res.drawable.ic_rabbit
        PetType.FISH -> Res.drawable.ic_fish
        PetType.TURTLE -> Res.drawable.ic_turtle
        PetType.SNAKE -> Res.drawable.ic_snake
        PetType.SPIDER -> Res.drawable.ic_spider
        PetType.HORSE -> Res.drawable.ic_horse
    }

val PetType.labelRes: StringResource
    get() = when (this) {
        PetType.CAT -> Res.string.pet_cat
        PetType.DOG -> Res.string.pet_dog
        PetType.PARROT -> Res.string.pet_parrot
        PetType.HAMSTER -> Res.string.pet_hamster
        PetType.RABBIT -> Res.string.pet_rabbit
        PetType.FISH -> Res.string.pet_fish
        PetType.TURTLE -> Res.string.pet_turtle
        PetType.SNAKE -> Res.string.pet_snake
        PetType.SPIDER -> Res.string.pet_spider
        PetType.HORSE -> Res.string.pet_horse
    }