<template>
  <div>
    <div class="calendar">
      <div
        v-for="day in days"
        :key="day"
        :class="['day', { selected: selectedDays.includes(day) }]"
        @click="toggleDay(day)"
      >
        {{ day }}
      </div>
    </div>
    <!-- <div class="selected-dates">
        Selected dates: {{ selectedDays.join(', ') }}
      </div> -->
  </div>
</template>
  
<script>
export default {
  props: {
    selectIds: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      days: Array.from({ length: 31 }, (_, i) =>
        i + 1 > 9 ? i + 1 : "0" + (i + 1)
      ),
      selectedDays: [],
    };
  },
  watch: {
    selectIds: {
      handler() {
        this.selectedDays = this.selectIds;
      },
      deep: true,
      immediate: true,
    },
  },
  methods: {
    toggleDay(day) {
      if (this.selectedDays.includes(day)) {
        this.selectedDays = this.selectedDays.filter((d) => d !== day);
      } else {
        this.selectedDays.push(day);
      }
      this.$emit("selectChange", this.selectedDays);
    },
  },
};
</script>
  
  <style>
.calendar {
  display: grid;
  padding: 10px;
  grid-template-columns: repeat(7, 1fr);
  gap: 10px;
}

.day {
  cursor: pointer;
  padding: 8px;
  border: 1px solid var(--base-color-6);
  border-radius: 50%;
  text-align: center;
}
.day:hover {
  border-color:  var(--current-color);
  color: var(--current-color);
}

.selected {
  /* background-color: #ccc; */
  border-color:  var(--current-color);
  color: var(--current-color);
}

.selected-dates {
  margin-top: 10px;
}
</style>
  