/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2016 Tobias Pietzsch, Stephan Preibisch, Stephan Saalfeld,
 * John Bogovic, Albert Cardona, Barry DeZonia, Christian Dietz, Jan Funke,
 * Aivar Grislis, Jonathan Hale, Grant Harris, Stefan Helfrich, Mark Hiner,
 * Martin Horn, Steffen Jaensch, Lee Kamentsky, Larry Lindsey, Melissa Linkert,
 * Mark Longair, Brian Northan, Nick Perry, Curtis Rueden, Johannes Schindelin,
 * Jean-Yves Tinevez and Michael Zinsmaier.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imglib2.type.numeric.array;

import net.imglib2.img.NativeImg;
import net.imglib2.img.NativeImgFactory;
import net.imglib2.img.basictypeaccess.IntAccess;
import net.imglib2.img.basictypeaccess.array.IntArray;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.util.Fraction;

/**
 * TODO
 *
 * @author Stephan Saalfeld
 */
public class IntArrayType extends AbstractArrayType< IntArrayType, IntAccess > implements NumericType< IntArrayType >
{
	// this is the constructor if you want it to read from an array
	public IntArrayType( final NativeImg< ?, ? extends IntAccess > longArrayStorage, final int length )
	{
		super( longArrayStorage, length );
	}

	// this is the constructor if you want it to be a variable
	public IntArrayType( final int[] values )
	{
		super( new IntArray( values ), values.length );
	}

	// this is the constructor if you want to specify the dataAccess
	public IntArrayType( final IntAccess access, final int length )
	{
		super( access, length );
	}

	// this is the constructor if you want it to be a variable
	public IntArrayType( final int length )
	{
		this( new int[ length ] );
	}

	@Override
	public NativeImg< IntArrayType, ? extends IntAccess > createSuitableNativeImg(
			final NativeImgFactory< IntArrayType > storageFactory,
			final long dim[] )
	{
		// create the container
		final NativeImg< IntArrayType, ? extends IntAccess > container =
				storageFactory.createIntInstance(
						dim,
						new Fraction( length, 1 ) );

		// create a Type that is linked to the container
		final IntArrayType linkedType = new IntArrayType( container, length );

		// pass it to the NativeContainer
		container.setLinkedType( linkedType );

		return container;
	}

	@Override
	public IntArrayType duplicateTypeOnSameNativeImg()
	{
		return new IntArrayType( img, length );
	}

	/**
	 * Return the i-th element of the array.
	 *
	 * @param i
	 * @return
	 */
	public int get( final int i )
	{
		return dataAccess.getValue( this.i + i );
	}

	public void set( final int f, final int i )
	{
		dataAccess.setValue( this.i + i, f );
	}

	@Override
	public int hashCode()
	{
		if ( length == 0 )
			return 0;

		int result = 1;
		for ( int j = 0; j < length; ++j )
			result = 31 * result + dataAccess.getValue( j );

		return result;
	}

	/**
	 * Create a variable array with one element.
	 */
	@Override
	public IntArrayType createVariable()
	{
		return new IntArrayType( 1 );
	}

	public IntArrayType createVariable( final int length )
	{
		return new IntArrayType( length );
	}

	@Override
	public IntArrayType copy()
	{
		final int[] copy = new int[ length ];
		for ( int j = 0; j < length; ++j )
			copy[ j ] = dataAccess.getValue( i + j );

		return new IntArrayType( copy );
	}

	@Override
	public void set( IntArrayType c )
	{
		final int l = Math.min( length, c.length );
		for ( int j = 0; j < l; ++j )
			set( c.get( j ), j );
	}

	@Override
	public boolean valueEquals( IntArrayType t )
	{
		if ( length != t.length )
			return false;
		for ( int j = 0; j < length; ++j )
			if ( t.get( j ) != get( j ) )
				return false;
		return true;
	}

	@Override
	public void add( IntArrayType c )
	{
		final int l = Math.min( length, c.length );
		for ( int j = 0; j < l; ++j )
		{
			final int k = i + j;
			dataAccess.setValue( k, dataAccess.getValue( k ) + c.get( j ) );
		}
	}

	@Override
	public void mul( IntArrayType c )
	{
		final int l = Math.min( length, c.length );
		for ( int j = 0; j < l; ++j )
		{
			final int k = i + j;
			dataAccess.setValue( k, dataAccess.getValue( k ) * c.get( j ) );
		}
	}

	@Override
	public void sub( IntArrayType c )
	{
		final int l = Math.min( length, c.length );
		for ( int j = 0; j < l; ++j )
		{
			final int k = i + j;
			dataAccess.setValue( k, dataAccess.getValue( k ) - c.get( j ) );
		}
	}

	@Override
	public void div( IntArrayType c )
	{
		final int l = Math.min( length, c.length );
		for ( int j = 0; j < l; ++j )
		{
			final int k = i + j;
			dataAccess.setValue( k, dataAccess.getValue( k ) / c.get( j ) );
		}
	}

	@Override
	public void setOne()
	{
		final int l = i + length;
		for ( int j = i; j < l; ++j )
			set( 1, j );
	}

	@Override
	public void setZero()
	{
		final int l = i + length;
		for ( int j = i; j < l; ++j )
			set( 0, j );
	}

	@Override
	public void mul( float c )
	{
		for ( int j = 0; j < length; ++j )
		{
			final int k = i + j;
			dataAccess.setValue( k, ( int )Math.floor( dataAccess.getValue( k ) * c ) );
		}
	}

	@Override
	public void mul( double c )
	{
		for ( int j = 0; j < length; ++j )
		{
			final int k = i + j;
			dataAccess.setValue( k, ( int )Math.floor( dataAccess.getValue( k ) * c ) );
		}
	}
}
