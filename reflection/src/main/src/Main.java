import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Save {
}

public class Main {

    public static void main(String[] args) {
        MyClass myObj = new MyClass(7, new Test(15L));
        String filename = "data.txt";

        try {
            myObj.save(filename);

            MyClass deserializationObj = new MyClass();
            deserializationObj.load(filename);
            System.out.println(deserializationObj.getField1());
            if (deserializationObj.getField2() != null) {
                System.out.println(deserializationObj.getField2().getField1());
            } else {
                System.out.println("Field 2 is null");
            }
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}

class MyClass {

    @Save
    private int field1;

    @Save
    private Test field2;

    public MyClass() {
        this.field2 = new Test();
    }

    public MyClass(int field1, Test field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    public int getField1() {
        return field1;
    }

    public void setField1(int field1) {
        this.field1 = field1;
    }

    public Test getField2() {
        return field2;
    }

    public void setField2(Test field2) {
        this.field2 = field2;
    }

    public void save(String filename) throws IOException, IllegalAccessException {
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
            saveFields(this, out);
        }
    }

    private void saveFields(Object obj, DataOutputStream out) throws IOException, IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Save.class)) {
                field.setAccessible(true);
                Class<?> type = field.getType();
                if (type == int.class) {
                    out.writeInt((int) field.get(obj));
                } else if (type == long.class) {
                    out.writeLong((long) field.get(obj));
                } else if (type == double.class) {
                    out.writeDouble((double) field.get(obj));
                } else if (type == float.class) {
                    out.writeFloat((float) field.get(obj));
                } else if (type == boolean.class) {
                    out.writeBoolean((boolean) field.get(obj));
                } else if (type == char.class) {
                    out.writeChar((char) field.get(obj));
                } else if (type == byte.class) {
                    out.writeByte((byte) field.get(obj));
                } else if (type == short.class) {
                    out.writeShort((short) field.get(obj));
                } else {
                    Object fieldValue = field.get(obj);
                    if (fieldValue != null) {
                        saveFields(fieldValue, out);
                    }
                }
            }
        }
    }

    public void load(String filename) throws IOException, IllegalAccessException {
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            loadFields(this, in);
        }
    }

    private void loadFields(Object obj, DataInputStream in) throws IOException, IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Save.class)) {
                field.setAccessible(true);
                Class<?> type = field.getType();
                if (type == int.class) {
                    field.setInt(obj, in.readInt());
                } else if (type == long.class) {
                    field.setLong(obj, in.readLong());
                } else if (type == double.class) {
                    field.setDouble(obj, in.readDouble());
                } else if (type == float.class) {
                    field.setFloat(obj, in.readFloat());
                } else if (type == boolean.class) {
                    field.setBoolean(obj, in.readBoolean());
                } else if (type == char.class) {
                    field.setChar(obj, in.readChar());
                } else if (type == byte.class) {
                    field.setByte(obj, in.readByte());
                } else if (type == short.class) {
                    field.setShort(obj, in.readShort());
                } else {
                    // Check if field is an object and recursively load its fields
                    Object fieldValue = field.get(obj);
                    if (fieldValue != null) {
                        loadFields(fieldValue, in);
                    }
                }
            }
        }
    }

}

class Test {
    @Save
    private long field1;

    public Test() {

    }

    public Test(long field1) {
        this.field1 = field1;
    }


    public long getField1() {
        return field1;
    }

    public void setField1(long field1) {
        this.field1 = field1;
    }
}
