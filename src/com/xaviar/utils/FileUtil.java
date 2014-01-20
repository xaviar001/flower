package com.xaviar.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class FileUtil {
	public static void channelCopy(final ReadableByteChannel src,

	final WritableByteChannel dest) throws IOException {

		final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
		while (src.read(buffer) != -1) {
			// prepare the buffer to be drained
			buffer.flip();
			// write to the channel, may block
			dest.write(buffer);
			// If partial transfer, shift remainder down
			// If buffer is empty, same as doing clear()
			buffer.compact();
		}
		// EOF will leave buffer in fill state
		buffer.flip();
		// make sure the buffer is fully drained.
		while (buffer.hasRemaining()) {
			dest.write(buffer);
		}
	}
	

	
	
	public static void streamCopy(final InputStream src, final OutputStream dest) throws IOException {
		ReadableByteChannel rbc = Channels.newChannel(src);
		java.nio.channels.WritableByteChannel wbc = Channels.newChannel(dest);
		FileUtil.channelCopy(rbc, wbc);
	}

}
