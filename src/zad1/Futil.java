package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;


public class Futil {

    public static void processDir(String dirName, String resultFileName) {
        Path dirPath = Paths.get(dirName);
        Path resultFilePath = Paths.get(resultFileName);
        try (FileChannel writeChannel = FileChannel.open(resultFilePath, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)
        ) {

            Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                    try (FileChannel readChannel = FileChannel.open(path)) {
                        ByteBuffer byteBuffer = ByteBuffer.allocateDirect((int) readChannel.size());
                        readChannel.read(byteBuffer);
                        byteBuffer.flip();
                        CharBuffer charBuffer = Charset.forName("Cp1250").decode(byteBuffer);
                        byteBuffer = StandardCharsets.UTF_8.encode(charBuffer);
                        writeChannel.write(byteBuffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException e)
                        throws IOException {
                    if (e == null) {
                        return FileVisitResult.CONTINUE;
                    } else {
                        throw e;
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
