/*
 * Copyright 2000-2012 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.android.designer.model;

import com.intellij.android.designer.designSurface.DropToOperation;
import com.intellij.android.designer.designSurface.TreeDropToOperation;
import com.intellij.designer.designSurface.EditOperation;
import com.intellij.designer.designSurface.OperationContext;

/**
 * @author Alexander Lobas
 */
public class RadViewAnimatorLayout extends RadViewLayout {
  public EditOperation processChildOperation(OperationContext context) {
    if ((context.isCreate() || context.isPaste() || context.isAdd() || context.isMove()) && checkChildOperation(context)) {
      if (context.isTree()) {
        return new TreeDropToOperation(myContainer, context);
      }
      return new DropToOperation((RadViewComponent)myContainer, context);
    }

    return null;
  }

  protected boolean checkChildOperation(OperationContext context) {
    return true;
  }
}