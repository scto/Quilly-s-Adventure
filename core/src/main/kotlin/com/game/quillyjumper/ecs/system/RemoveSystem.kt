package com.game.quillyjumper.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.game.quillyjumper.ecs.component.*
import ktx.ashley.allOf

class RemoveSystem(engine: Engine) : IteratingSystem(allOf(RemoveComponent::class).get()), EntityListener {
    private val collisionEntities = engine.getEntitiesFor(allOf(CollisionComponent::class).get())
    private val damageEntities = engine.getEntitiesFor(allOf(DamageComponent::class).get())
    private val aggroEntities = engine.getEntitiesFor(allOf(AggroComponent::class).get())

    override fun addedToEngine(engine: Engine) {
        engine.addEntityListener(this)
        super.addedToEngine(engine)
    }

    override fun removedFromEngine(engine: Engine) {
        engine.removeEntityListener(this)
        super.removedFromEngine(engine)
    }

    override fun entityAdded(entity: Entity?) {
        // do nothing
    }

    override fun entityRemoved(entity: Entity?) {
        // cleanup entity from any arrays where it is referenced
        collisionEntities.forEach { it.collCmp.entities.remove(entity) }
        damageEntities.forEach { it.damageCmp.damagedEntities.remove(entity) }
        aggroEntities.forEach { it.aggroCmp.aggroEntities.remove(entity) }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        engine.removeEntity(entity)
    }
}