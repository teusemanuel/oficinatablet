/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.oficinatablet.utils;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mateus Emanuel Araújo on 9/27/16.
 * MA Solutions
 * teusemanuel@gmail.com
 */
public abstract class SelectableFirebaseAdapter<T, VH extends RecyclerView.ViewHolder> extends FirebaseRecyclerAdapter<T, VH> {

    private static final String TAG = "SelectableFirebaseAdapter";

    private SparseBooleanArray selectedItems;

    public SelectableFirebaseAdapter(Class<T> modelClass, int modelLayout, Class<VH> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        selectedItems = new SparseBooleanArray();
    }

    public SelectableFirebaseAdapter(Class<T> modelClass, int modelLayout, Class<VH> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, (Query) ref);
        selectedItems = new SparseBooleanArray();
    }

    /**
     * Indica se o item na posição posição for selecionada
     * @param position Posição do item para verificar
     * @return true se o item é selecionado, caso contrário false
     */
    public boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    /**
     * Alternar o status de seleção do item em uma determinada posição
     * @param position Posição do item para alternar o status de seleção
     */
    public void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    /**
     * Limpar o status de seleção para todos os itens
     */
    public void clearSelection() {
        List<Integer> selection = getSelectedItems();
        selectedItems.clear();
        for (Integer i : selection) {
            notifyItemChanged(i);
        }
    }

    /**
     * Contar os itens selecionados
     * @return A quantidade de itens selecionados
     */
    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    /**
     * Indica a lista de itens selecionados
     * @return Lista de itens selecionados ids
     */
    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); ++i) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    /**
     * Indica a lista de itens selecionados
     * @return Lista de itens selecionados ids
     */
    public Map<String,T> getSelectedObjects() {
        Map<String,T> items = new HashMap<>(selectedItems.size());

        for (int i = 0; i < selectedItems.size(); ++i) {
            int selectedPosition = selectedItems.keyAt(i);
            T object = getItem(selectedPosition);
            String objectKey = getRef(selectedPosition).getKey();
            items.put(objectKey, object);
        }

        return items;
    }
}
