package williewillus.BugfixMod.patchers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

/**
 * Created by Vincent on 3/11/14.
 */


public class ChatOpacityFixPatcher {
    public static byte[] patch(byte[] bytes, boolean isObf) {
        String targetMethodName, targetMethodDesc;
        if (isObf) {
            targetMethodName = "a";
            targetMethodDesc = "(I)V";
        } else {
            targetMethodName = "drawChat";
            targetMethodDesc = "(I)V";
        }

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);


        for (MethodNode m : classNode.methods) {
            if (m.name.equals(targetMethodName) && m.desc.equals(targetMethodDesc)) {
                System.out.println("[ChatOpacityFix] Found target method: " + targetMethodName);

                AbstractInsnNode currentInstruction;
                Iterator<AbstractInsnNode> instructionSet = m.instructions.iterator();

                int index = 0;


                while (instructionSet.hasNext()) {
                    currentInstruction = instructionSet.next();
                    if (currentInstruction.getOpcode() == Opcodes.INVOKESTATIC && currentInstruction.getPrevious().getOpcode() == Opcodes.ISHL && currentInstruction.getPrevious().getPrevious().getOpcode() == Opcodes.BIPUSH) {
                        System.out.println("[ChatOpacityFix] Entry point found:" + (index + 2));
                        AbstractInsnNode entryPoint = currentInstruction.getNext().getNext();
                        InsnList toInject = new InsnList();
                        toInject.add(new IntInsnNode(Opcodes.SIPUSH, 3042));
                        toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/lwjgl/opengl/GL11", "glEnable", "(I)V")); // Simply insert a glEnable call that was present in 1.6.4 but absent in 1.7.x
                        m.instructions.insert(entryPoint, toInject);
                    }
                    index++;
                }


            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        System.out.println("[ChatOpacityFix] Applied Transform!");
        return writer.toByteArray();
    }

}
