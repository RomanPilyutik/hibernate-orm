/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2011, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */

package org.hibernate.metamodel.internal.source.annotations.attribute.type;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.metamodel.internal.source.annotations.attribute.MappedAttribute;
import org.hibernate.metamodel.internal.source.annotations.util.JPADotNames;
import org.hibernate.metamodel.internal.source.annotations.util.JandexHelper;
import org.hibernate.type.CharacterArrayClobType;
import org.hibernate.type.PrimitiveCharacterArrayClobType;
import org.hibernate.type.SerializableToBlobType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.WrappedMaterializedBlobType;
import org.hibernate.usertype.DynamicParameterizedType;

import org.jboss.jandex.AnnotationInstance;

/**
 * @author Strong Liu
 * @author Brett Meyer
 */
public class LobTypeResolver extends AbstractAttributeTypeResolver {

	public LobTypeResolver(MappedAttribute mappedAttribute) {
		super( mappedAttribute );
	}

	@Override
	protected AnnotationInstance getTypeDeterminingAnnotationInstance() {
		return JandexHelper.getSingleAnnotation( mappedAttribute.annotations(), JPADotNames.LOB );
	}

	@Override
	public String resolveAnnotatedHibernateTypeName(AnnotationInstance annotationInstance) {
		if ( annotationInstance == null ) {
			//only check attributes annotated with @Lob
			return null;
		}
		String type = "blob";
		if ( Clob.class.isAssignableFrom( mappedAttribute.getAttributeType() ) ) {
			type = StandardBasicTypes.CLOB.getName();
		}
		else if ( Blob.class.isAssignableFrom( mappedAttribute.getAttributeType() ) ) {
			type = StandardBasicTypes.BLOB.getName();
		}
		else if ( String.class.isAssignableFrom( mappedAttribute.getAttributeType() ) ) {
			type = StandardBasicTypes.MATERIALIZED_CLOB.getName();
		}
		else if ( Character[].class.isAssignableFrom( mappedAttribute.getAttributeType() ) ) {
			type = CharacterArrayClobType.class.getName();
		}
		else if ( char[].class.isAssignableFrom( mappedAttribute.getAttributeType() ) ) {
			type = PrimitiveCharacterArrayClobType.class.getName();
		}
		else if ( Byte[].class.isAssignableFrom( mappedAttribute.getAttributeType() ) ) {
			type = WrappedMaterializedBlobType.class.getName();
		}
		else if ( byte[].class.isAssignableFrom( mappedAttribute.getAttributeType() ) ) {
			type = StandardBasicTypes.MATERIALIZED_BLOB.getName();
		}
		else if ( Serializable.class.isAssignableFrom( mappedAttribute.getAttributeType() ) ) {
			type = SerializableToBlobType.class.getName();
		}
		return type;
	}

	@Override
	protected Map<String, String> resolveHibernateTypeParameters(AnnotationInstance annotationInstance) {
		return Collections.emptyMap();
	}
}