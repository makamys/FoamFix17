/*
 * Copyright (c) 2015 Vincent Lee
 * Copyright (c) 2020, 2021 Adrian "asie" Siekierka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package pl.asie.foamfix.bugfixmod.coremod.patchers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import pl.asie.foamfix.bugfixmod.coremod.MappingRegistry;

import java.util.Iterator;

/**
 * Created by Vincent on 6/6/2014.
 *
 * This tweak makes chickens follow anything they can breed with.
 */
public class ChickenLureTweakPatcher extends AbstractPatcher {

    public ChickenLureTweakPatcher(String name, String targetClassName, String targetMethodName, String targetMethodDesc) {
        super(name, targetClassName, targetMethodName, targetMethodDesc);
    }

    @Override
    public InsnList buildNewInsns(AbstractInsnNode currentInstruction, Iterator<AbstractInsnNode> instructionSet) {
        if (currentInstruction instanceof MethodInsnNode) {

            MethodInsnNode currentMIN = (MethodInsnNode) currentInstruction;

            if (currentMIN.getOpcode() == Opcodes.INVOKESPECIAL && currentMIN.getNext().getOpcode() == Opcodes.INVOKEVIRTUAL) {
                String innerTargetOwner = ("net/minecraft/entity/ai/EntityAITempt");

                if (currentMIN.owner.equals(innerTargetOwner)) {
                    printMessage("Found entry point");
                    String chickenName = ("net/minecraft/entity/passive/EntityChicken");
                    String chickenTasks = MappingRegistry.getFieldNameFor("EntityChicken.tasks");
                    String aiTaskName = ("net/minecraft/entity/ai/EntityAITasks");
                    String aiTaskAdd = MappingRegistry.getMethodNameFor("EntityAITasks.addTask");
                    String aiTemptName = ("net/minecraft/entity/ai/EntityAITempt");
                    String aiBaseName = ("net/minecraft/entity/ai/EntityAIBase");
                    String creatureName = ("net/minecraft/entity/EntityCreature");
                    String initItemName = ("net/minecraft/init/Items");
                    String itemName = ("net/minecraft/item/Item");
                    String melonName = MappingRegistry.getFieldNameFor("Items.melon_seeds");
                    String pumpkinName = MappingRegistry.getFieldNameFor("Items.pumpkin_seeds");
                    String wartName = MappingRegistry.getFieldNameFor("Items.nether_wart");

                    InsnList toInject = new InsnList();

                    //Melon seeds
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    toInject.add(new FieldInsnNode(Opcodes.GETFIELD, chickenName, chickenTasks, "L" + aiTaskName + ";"));
                    toInject.add(new InsnNode(Opcodes.ICONST_3));
                    toInject.add(new TypeInsnNode(Opcodes.NEW, aiTemptName));
                    toInject.add(new InsnNode(Opcodes.DUP));
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    toInject.add(new InsnNode(Opcodes.DCONST_1));
                    toInject.add(new FieldInsnNode(Opcodes.GETSTATIC, initItemName, melonName, "L" + itemName + ";"));
                    toInject.add(new InsnNode(Opcodes.ICONST_0));
                    toInject.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, aiTemptName, "<init>", "(L" + creatureName + ";DL" + itemName + ";Z)V"));
                    toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, aiTaskName, aiTaskAdd, "(IL" + aiBaseName + ";)V"));

                    //Pumpkin seeds
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    toInject.add(new FieldInsnNode(Opcodes.GETFIELD, chickenName, chickenTasks, "L" + aiTaskName + ";"));
                    toInject.add(new InsnNode(Opcodes.ICONST_3));
                    toInject.add(new TypeInsnNode(Opcodes.NEW, aiTemptName));
                    toInject.add(new InsnNode(Opcodes.DUP));
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    toInject.add(new InsnNode(Opcodes.DCONST_1));
                    toInject.add(new FieldInsnNode(Opcodes.GETSTATIC, initItemName, pumpkinName, "L" + itemName + ";"));
                    toInject.add(new InsnNode(Opcodes.ICONST_0));
                    toInject.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, aiTemptName, "<init>", "(L" + creatureName + ";DL" + itemName + ";Z)V"));
                    toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, aiTaskName, aiTaskAdd, "(IL" + aiBaseName + ";)V"));

                    //Netherwart
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    toInject.add(new FieldInsnNode(Opcodes.GETFIELD, chickenName, chickenTasks, "L" + aiTaskName + ";"));
                    toInject.add(new InsnNode(Opcodes.ICONST_3));
                    toInject.add(new TypeInsnNode(Opcodes.NEW, aiTemptName));
                    toInject.add(new InsnNode(Opcodes.DUP));
                    toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    toInject.add(new InsnNode(Opcodes.DCONST_1));
                    toInject.add(new FieldInsnNode(Opcodes.GETSTATIC, initItemName, wartName, "L" + itemName + ";"));
                    toInject.add(new InsnNode(Opcodes.ICONST_0));
                    toInject.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, aiTemptName, "<init>", "(L" + creatureName + ";DL" + itemName + ";Z)V"));
                    toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, aiTaskName, aiTaskAdd, "(IL" + aiBaseName + ";)V"));

                    successful = true;
                }
            }
        }

        return null;
    }
}
