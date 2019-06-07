package net.dlcruz.springfox

import com.fasterxml.classmate.ResolvedType
import com.fasterxml.classmate.TypeResolver
import springfox.documentation.schema.AlternateTypeRule

class RecursiveAlternateTypeRule(
        typeResolver: TypeResolver,
        private val rules: List<AlternateTypeRule>):
        AlternateTypeRule(typeResolver.resolve(Object::class.java), typeResolver.resolve(Object::class.java))
{

    override fun alternateFor(type: ResolvedType): ResolvedType {
        val newType = rules.map { it.alternateFor(type) }.firstOrNull { it != type } ?: type
        return if (appliesTo(newType)) alternateFor(newType) else newType
    }

    override fun appliesTo(type: ResolvedType?) = rules.any { it.appliesTo(type) }
}